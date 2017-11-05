package ch.agilesolutions.demo.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import ch.agilesolutions.demo.cdi.SystemProperty;
import ch.agilesolutions.demo.model.Profile;

@Stateless
public class DocumentDao {

	private static final String DOMAIN_DIR = System.getProperty("service.odo.staging") + "/database/profiles";

	@Inject
	private Logger logger;

	@Inject
	@SystemProperty("service.odo.gituser")
	String gitUser;

	@Inject
	@SystemProperty("service.odo.gitpassword")
	String gitPassword;

	@Inject
	MongoClient mongoClient;

	public Profile save(Profile profile, String comment) {

		GsonBuilder gsonBuilder = new GsonBuilder();

		DB db = mongoClient.getDB("demo");

		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		try {


			DBCollection coll = db.getCollection("profilestore");
			
	        BasicDBObject newDocument = new BasicDBObject();

			DBObject searchQuery = (DBObject) JSON.parse(gson.toJson(profile));
			coll.update(searchQuery, newDocument);

		} catch (Exception e) {
			logger.error("Error saving profile ", e);
			throw new IllegalStateException(e);
		}

		return profile;

	}

	public Profile delete(Profile profile) {

		String fileName = DOMAIN_DIR + File.separator + profile.getName() + ".json";

		try {
			File file = new File(fileName);

			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.close();

			if (file.delete()) {

				GitCache.getGit().add().setUpdate(true).addFilepattern(".").call();

				GitCache.getGit().commit().setCommitter(gitUser, "robert.rong@agile-solutions.com")
						.setMessage(String.format("Profile %s deleted!", profile.getName())).call();

			} else {
				throw new IllegalStateException("Delete operation is failed.");

			}

		} catch (Exception e) {
			logger.error("Error deleting profile ", e);
			throw new IllegalStateException(e);
		}

		return profile;
	}

	public void synchronizeRemoteGIT() {
		try {

			GitCache.getGit().push()
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUser, gitPassword)).call();
		} catch (Exception e) {
			logger.error("Error synchronizing remote repository", e);
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 
	 * http://www.leveluplunch.com/java/examples/read-file-into-string/
	 */
	public List<Profile> findAll() {

		Gson gson = new Gson();

		List<Profile> profiles = new ArrayList<>();

		try {
			Files.walk(Paths.get(DOMAIN_DIR)).filter(Files::isRegularFile).forEach(((path) -> {
				try {

					String stringFromFile = java.nio.file.Files.lines(path).collect(Collectors.joining());

					Profile importProfile = gson.fromJson(stringFromFile, Profile.class);

					profiles.add(importProfile);
				} catch (Exception e) {
					logger.error("Error reading profile files ", e);
					throw new IllegalStateException(e);
				}
			}));
		} catch (IOException e) {
			logger.error("Error reading profile files ", e);
			throw new IllegalStateException(e);
		}

		return profiles;

	}

	/**
	 * 
	 * http://www.leveluplunch.com/java/examples/read-file-into-string/
	 */
	public List<Profile> findByProfile(final String id) {

		try {
			GitCache.getGit().pull().call();
		} catch (GitAPIException e) {
			logger.error("Error synchronizing GIT repository ", e);
			throw new IllegalStateException(e);

		}

		Gson gson = new Gson();

		List<Profile> profiles = new ArrayList<>();

		try {
			Files.walk(Paths.get(DOMAIN_DIR)).filter(Files::isRegularFile).forEach(((path) -> {
				try {

					String stringFromFile = java.nio.file.Files.lines(path).collect(Collectors.joining());

					Profile importProfile = gson.fromJson(stringFromFile, Profile.class);

					if (importProfile.getName().equalsIgnoreCase(id)) {
						profiles.add(importProfile);
					}
				} catch (Exception e) {
					logger.error("Error reading profile files ", e);
					throw new IllegalStateException(e);
				}
			}));
		} catch (IOException e) {
			logger.error("Error reading profile files ", e);
			throw new IllegalStateException(e);
		}

		return profiles;

	}
}
