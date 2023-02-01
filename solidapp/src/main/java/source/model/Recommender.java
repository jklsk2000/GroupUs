package source.model;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;

import java.util.ArrayList;
import java.util.Arrays;

public final class Recommender {
    /**
     * Final class meant to enventually store code for Factory methods for Groups / recommendations for groups.
     */

    private Recommender(){}
    private static double invertedDistance(double a, double b, double maxVal, double minVal){
        /**
         * Method that takes in two doubles and the min / max allowed values and returns an inverted distance metric.
         * @param a first response value
         * @param b second response value
         * @param maxVal maximum value either response could be.
         * @param minVal min val either response value could be.
         * @return range - distance between a and b.
         */
        if (maxVal < minVal || a < minVal || a > maxVal || b < minVal || b > maxVal) {
            throw new IllegalArgumentException();
        }

        double abDistance = Recommender.distanceComponent(a, b);
        double largestDistance = Recommender.distanceComponent(maxVal,minVal);
        return (largestDistance - abDistance)/largestDistance;
    }
    private static double distanceComponent(double valA, double valB){
        /**
         * Simple helper to return the distance.
         * @param valA first value
         * @param valB second point
         * @return distance between valA and valB.
         */
        return Math.pow(valA-valB,2);
    }

    public static double getCompatibilityRanking(SimpleSurveyResponse left, SimpleSurveyResponse right) {
        /**
         * Static helper, takes in two surveys and returns an estimate of how well they would potentially work together,
         * where lower numbers mean a better match.
         * @param left one person's survey response to compare
         * @param right the other person's survey response to compare for compatibility
         * @return a double value representing the compatibility of the two respondents, the lower the number the
         * better.
         */
        //TODO: ensure here that both surveys are the same...
        System.out.println(left.getUsername());
        System.out.println(right.getUsername());
        Survey survey = left.getSurvey();
        if (left.length() != right.length()){
            throw new IllegalArgumentException();
        }
        double modifiedDistance = 0;
        double range;
        int numElements = left.length();
        SimpleQuestion currQ;
        double currWeight;
        double curr;
        for(int i = 0; i < numElements; i++) {
            currQ = survey.getQuestion(i);
            range = Recommender.distanceComponent(currQ.getMaxVal(),currQ.getMinVal());
            currWeight = currQ.getWeight();
            if(currWeight >= 0) {
                curr = currWeight * distanceComponent(left.getAnswer(i), right.getAnswer(i))/range;
            } else {
                curr = Math.abs(currWeight) * invertedDistance(left.getAnswer(i),
                        right.getAnswer(i),
                        currQ.getMaxVal(),
                        currQ.getMinVal());
            }
            modifiedDistance += curr;
        }
        return Math.sqrt(modifiedDistance);
    }


    private static long[][] getDistanceMatrix(ArrayList<SimpleSurveyResponse> responses) {
        long[][] distanceMatrix = new long[responses.size()+1][responses.size()+1]; //want extra space on first row and
        //column
        int row_index;
        int col_index;
        double value;
        for (int rawIndex = 0; rawIndex < responses.size(); rawIndex++) {
            row_index = rawIndex + 1; //everything shifted by one to leave first 0
            SimpleSurveyResponse currRowSurvey = responses.get(rawIndex);
            for (int rawColIndex = rawIndex + 1; rawColIndex < responses.size(); rawColIndex++) {
                col_index = rawColIndex + 1;

                value = Recommender.getCompatibilityRanking(currRowSurvey, responses.get(rawColIndex));
                distanceMatrix[row_index][col_index] =  (long)  (10000 * value);
                distanceMatrix[col_index][row_index] = distanceMatrix[row_index][col_index];
            }
        }
        return distanceMatrix;

    }

    public static ArrayList<ArrayList<String>> generateGroupArray(long totalDistance, ArrayList<String> oneGroupNames) {

        ArrayList<ArrayList<String>> groupNames;
        groupNames = new ArrayList<ArrayList<String>>();


        return groupNames;
    }

    public static ArrayList<ArrayList<String>> matchParticipants(Survey s) {
        // Prep code
        final int startPlaceholder = 0;
        ArrayList<SimpleSurveyResponse> responses = s.getResponses();

        int maxPerGroup = s.getMaxPerGroup();
        int numParticipants = responses.size();

        if (numParticipants < 1) {
            return new ArrayList<ArrayList<String>>();
        }

        int numGroups = (int) Math.ceil((double) numParticipants / maxPerGroup);

        long[][] distances = getDistanceMatrix(s.getResponses());
        long[] spacesUsedPerSubgroup = new long[numParticipants + 1];

        for (int i = 1; i < numParticipants + 1; i++) {
            spacesUsedPerSubgroup[i] = 1;
        }

        long[] group_capacity = new long[numGroups];
        Arrays.fill(group_capacity, maxPerGroup);

        String[] names = new String[numParticipants+1];
        names[0] = "starting_point";

        for (int i = 1; i < numParticipants+1; i++) {
            names[i] = "" + responses.get(i-1).getId(); //switching to string uid
        }

        // capacity based vehicle routing optimizer section, adapted from OR tools capacity constrained vrm samples
        Loader.loadNativeLibraries();

        RoutingIndexManager manager = new RoutingIndexManager(distances.length, numGroups, startPlaceholder);
        RoutingModel routing = new RoutingModel(manager);

        Assignment solution = getSolution( manager,  routing, distances, spacesUsedPerSubgroup, group_capacity);


            // Solution cost.

        return getGroupNames( numGroups,  routing,  manager,  solution, names);

    }

    public static Assignment getSolution(RoutingIndexManager manager, RoutingModel routing, long[][] distances,long[] spacesUsedPerSubgroup,long[] group_capacity){
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return distances[fromNode][toNode];
                });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            // Convert from routing variable Index to user NodeIndex.
            int fromNode = manager.indexToNode(fromIndex);
            return spacesUsedPerSubgroup[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, // null capacity slack
                group_capacity, // vehicle maximum capacities
                true, // start cumul to zero
                "Capacity");
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(Duration.newBuilder().setSeconds(1).build())
                        .build();
        return routing.solveWithParameters(searchParameters);
    }

    public static ArrayList<ArrayList<String>> getGroupNames(int numGroups, RoutingModel routing, RoutingIndexManager manager, Assignment solution, String[] names){
        ArrayList<ArrayList<String>> groupNames = new ArrayList<ArrayList<String>>();
        ArrayList<String> oneGroupNames;
        for (int i = 0; i < numGroups; ++i) {
            oneGroupNames = new ArrayList<String>();

            long index = routing.start(i);
            long groupDistance = 0;
            while (!routing.isEnd(index)) {
                long nodeIndex = manager.indexToNode(index);
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                groupDistance += routing.getArcCostForVehicle(previousIndex,index,i);
                if ((int) nodeIndex != 0) {
                    // we keep the starting_point in our names list for our optimization to be
                    // able to freely start/end from any person (starting_point is a person with distance of 0 to
                    // everyone else both ways that all groups must include) but we don't actually want starting_point
                    // in our suggested team member names
                    oneGroupNames.add(names[(int) nodeIndex]);
                }
            }
            groupNames.add(oneGroupNames);
        }
        return groupNames;
    }

}


