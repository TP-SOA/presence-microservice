package fr.iss.soa.presencemicroservice;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class PresenceDetectorController {
    private final ArrayList<PresenceDetector> presenceDetectors = new ArrayList<>();

    private final String centralName = "central-microservice";

    @Autowired
    private EurekaClient eurekaClient;

    @PostConstruct
    public void init() {
        //Simulate a presence in room 11 every 5 seconds
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendPresenceDetected(11);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5*1000);
        //Simulate a presence in room 213 every 30 seconds
        Timer t1 = new Timer();
        t1.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendPresenceDetected(213);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 30*1000);
        //Simulate a presence in room 114 every 30 seconds
        Timer t2 = new Timer();
        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendPresenceDetected(114);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 30*1000);
    }

    private String getBaseServiceUrl(String serviceName) {
        InstanceInfo service = eurekaClient
            .getApplication(serviceName)
            .getInstances()
            .get(0);

        String hostName = service.getHostName();
        int port = service.getPort();
        return "http://" + hostName + ":" + port;
    }

    @GetMapping("/detectors")
    public ArrayList<PresenceDetector> getDetectors() {

        //Simulate DB
        if (presenceDetectors.isEmpty()) {
            presenceDetectors.add(new PresenceDetector(11, "Toto"));
            presenceDetectors.add(new PresenceDetector(114, "Tata"));
            presenceDetectors.add(new PresenceDetector(7, "Tutu"));
            presenceDetectors.add(new PresenceDetector(116, "Tete"));
            presenceDetectors.add(new PresenceDetector(213, "Tyty"));
        }

        return presenceDetectors;
    }

    @GetMapping("/detectors/{detector_id}")
    public PresenceDetector getDetector(@PathVariable("detector_id") int id) {
        Optional<PresenceDetector> opt = presenceDetectors.stream().filter(i -> i.getId() == id).findFirst();
        return opt.orElseThrow(() -> new PresenceDetectorNotFoundException(id));
    }

    public void sendPresenceDetected(int id) throws URISyntaxException {
        System.out.println("Trying to execute POST request to " + id);

        URI centralService = new URI(getBaseServiceUrl(centralName) + "/presence-event/" + id);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(centralService, null, String.class);

            if (response.getStatusCode() != HttpStatus.OK)
                System.err.println("Oops, " + id + " does not exist.");
        } catch (ResourceAccessException e) {
            e.printStackTrace();
        }
    }

}
