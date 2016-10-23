package uk.ac.dundee.computing.ej.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instagrim.Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String CreateUserPicTable = "CREATE TABLE if not exists instagrim.userPics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " imagelength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (user)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists instagrim.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user, pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String CreateUserProfile = "CREATE TABLE if not exists instagrim.userprofiles (\n"
                    + "     login text PRIMARY KEY,\n"
                    + "     password text,\n"
                    + "     first_name text,\n"
                    + "     last_name text,\n"
                    + "     email text,\n"
                    + "  );";
            String CreateCommentTable = "CREATE TABLE if not exists instagrim.comments (\n"
                    + "     picid uuid, \n"
                    + "     cmntid uuid PRIMARY KEY, \n"
                    + "     cmnt_added timestamp,\n"
                    + "     user text, \n"
                    + "     comment text,\n"
                    + "  );";
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("Created instagrim ");
            } catch (Exception et) {
                System.out.println("Can't create instagrim keyspace " + et);
            }

            //now add some column families 
            System.out.println("" + CreatePicTable);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create picture table " + et);
            }
            
            System.out.println("" + CreateUserPicTable);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserPicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create profile picture table " + et);
            }
            
            System.out.println("" + Createuserpiclist);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            
            System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user profile table " + et);
            }
            
            //now add some column families 
            System.out.println("" + CreateCommentTable);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateCommentTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create comment table " + et);
            }
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
