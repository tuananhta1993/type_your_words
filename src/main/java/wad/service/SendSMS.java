/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuananhta
 */
@Service
public class SendSMS {
    public void send(String to_number, String content) throws MalformedURLException, IOException {
        if (to_number.length()>4) {
            to_number="0465964478"; // Test
            content = URLEncoder.encode(content, "UTF-8");
            
            URL url = new URL("http://sms.trafore.com/sms_request?numbers=0465964478&message="+content+"&API=tuananhta28081993");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
        }
        }
    }
}
