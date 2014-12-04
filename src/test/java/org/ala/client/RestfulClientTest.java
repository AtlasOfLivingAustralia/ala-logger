package org.ala.client;

import org.ala.client.util.Constants;
import org.ala.client.util.LoggingContext;
import org.ala.client.util.RestfulClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RestfulClient.class})
public class RestfulClientTest {

    @Test
    public void testUserAgentSetToUndefinedIfNotProvidedInTheContext() throws Exception {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.getParams()).thenReturn(Mockito.mock(HttpClientParams.class));

        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        RestfulClient client = new RestfulClient(0);
        client.restPost("someurl", message);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        Mockito.verify(mockHttpClient).executeMethod(captor.capture());


        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals(Constants.UNDEFINED_USER_AGENT_VALUE, sentValue);
    }

    @Test
    public void testUserAgentSetToValueProvidedInContext() throws Exception {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.getParams()).thenReturn(Mockito.mock(HttpClientParams.class));

        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        LoggingContext.setProperty(Constants.USER_AGENT_PARAM, "myUserAgent");

        RestfulClient client = new RestfulClient(0);
        client.restPost("someurl", message);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        Mockito.verify(mockHttpClient).executeMethod(captor.capture());

        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals("myUserAgent", sentValue);
    }
}
