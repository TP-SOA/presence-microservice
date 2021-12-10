package fr.iss.soa.presencemicroservice;

public class PresenceDetectorNotFoundException extends RuntimeException {

    PresenceDetectorNotFoundException(long id) {
        super("Could not find presence detector " + id);
    }
}
