/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asu.sampleAdapter;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The www.Sample.com producer.
 */
public class sampleAdapterProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(sampleAdapterProducer.class);
    private sampleAdapterEndpoint endpoint;
    private static final String USER_AGENT = "Mozilla/5.0";
    static String GET_URL = "";


	public sampleAdapterProducer(sampleAdapterEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    public void process(final Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);
		String greetingMessage = endpoint.getUrl();
		if(greetingMessage == null || greetingMessage.isEmpty()) {
			greetingMessage = "(Producer) Hello!";
		}
        String GET_URL;
        GET_URL = greetingMessage;
        String result = sendGET(GET_URL);

        String messageInUpperCase = greetingMessage.toLowerCase();
        if (input != null) {
            LOG.debug(input);
            messageInUpperCase = "Input from Runtime Dip " + input + "\n Response from " + result + " \n URL: " + messageInUpperCase;
            System.out.print(messageInUpperCase + "\n");
        }
        exchange.getIn().setBody(messageInUpperCase);
    }

    private static String sendGET(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        String output = null;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            output = response.toString();
        } else {
            System.out.println("GET request not worked");
            output = "GET request not worked";
        }
        return output;
    }

}
