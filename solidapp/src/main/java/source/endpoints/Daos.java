package source.endpoints;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import source.model.*;

public class Daos {

    public static ConnectionSource connectionSource = null;

    /**
     * When running locally, use the SQLite database.
     * When running via Heroku, use the compatible PostgreSQL database.
     * Return the connection source accordingly.
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static void getConnectionSource() throws SQLException, URISyntaxException {
        String databaseURL = System.getenv("DATABASE_URL");
        String uri = "";
        if (connectionSource == null) {
            if (databaseURL == null) {
                uri = "jdbc:sqlite:./SolidApp.db";
                connectionSource = new JdbcConnectionSource(uri);
            } else {
                URI dbURI = new URI(databaseURL);
                String username = dbURI.getUserInfo().split(":")[0];
                String password = dbURI.getUserInfo().split(":")[1];
                uri = "jdbc:postgresql://" + dbURI.getHost() + ':'
                        + dbURI.getPort() + dbURI.getPath() + "?sslmode=require";
                connectionSource = new JdbcConnectionSource(uri, username, password);
            }
        }
    }

    public static Dao getStudentORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao studentDao = DaoManager.createDao(connectionSource, Student.class);
        Dao partInfoDao = DaoManager.createDao(connectionSource, ParticipantInfo.class);

        if(!studentDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Student.class);
        }
        if(!partInfoDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, ParticipantInfo.class);
        }

        return studentDao;
    }

    public static Dao getInstructorORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao eventDao = DaoManager.createDao(connectionSource, Event.class);

        if(!eventDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
        }
        Dao participantInfoDao = DaoManager.createDao(connectionSource,ParticipantInfo.class);
        if(!participantInfoDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, ParticipantInfo.class);
        }
        Dao instructorDao = DaoManager.createDao(connectionSource,Instructor.class);
        if(!instructorDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Instructor.class);
        }

        return instructorDao;
    }

    public static Dao getEventORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao groupDao = DaoManager.createDao(connectionSource, Group.class);

        if(!groupDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
        }
        Dao eventDao = DaoManager.createDao(connectionSource, Event.class);
        if(!eventDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
        }

        return eventDao;
    }

    public static Dao getGroupORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao groupDao = DaoManager.createDao(connectionSource, Group.class);

        if(!groupDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
        }

        return groupDao;
    }

    public static Dao getResponseORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao responseDao = DaoManager.createDao(connectionSource, SimpleSurveyResponse.class);

        if(!responseDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, SimpleSurveyResponse.class);
        }

        return responseDao;
    }

    public static Dao getQuestionORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao questionDao = DaoManager.createDao(connectionSource, SimpleQuestion.class);

        if(!questionDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, SimpleQuestion.class);
        }

        return questionDao;
    }

    public static Dao getSurveyORMLiteDao() throws SQLException, URISyntaxException {
        getConnectionSource();

        Dao surveyDao = DaoManager.createDao(connectionSource, Survey.class);
        if(!surveyDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, Survey.class);
        }

        return surveyDao;
    }
}
