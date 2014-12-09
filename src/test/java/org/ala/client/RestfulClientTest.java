package org.ala.client;

import org.ala.client.appender.RestfulAppender;
import org.ala.client.util.Constants;
import org.ala.client.util.RestfulClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.spi.LoggingEvent;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RestfulClient.class, RestfulAppender.class})
public class RestfulClientTest {

    @Test
    public void testUserAgentSetToUndefinedIfNotProvidedInTheContext() throws Exception {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.getParams()).thenReturn(Mockito.mock(HttpClientParams.class));
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        Mockito.when(mockMapper.getDeserializationConfig()).thenReturn(Mockito.mock(DeserializationConfig.class));
        Mockito.when(mockMapper.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        PowerMockito.whenNew(ObjectMapper.class).withAnyArguments().thenReturn(mockMapper);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        RestfulAppender appender = new RestfulAppender();
        appender.setUrlTemplate("somurl");
        LoggingEvent event = Mockito.mock(LoggingEvent.class);
        Mockito.when(event.getMessage()).thenReturn(message);
        appender.doAppend(event);

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

        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        Mockito.when(mockMapper.getDeserializationConfig()).thenReturn(Mockito.mock(DeserializationConfig.class));
        Mockito.when(mockMapper.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        PowerMockito.whenNew(ObjectMapper.class).withAnyArguments().thenReturn(mockMapper);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        RestfulAppender appender = new RestfulAppender();
        appender.setUrlTemplate("somurl");
        LoggingEvent event = Mockito.mock(LoggingEvent.class);
        Mockito.when(event.getMessage()).thenReturn(message);
        Mockito.when(event.getMDC(Mockito.eq(Constants.USER_AGENT_PARAM))).thenReturn("myUserAgent");
        appender.doAppend(event);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        Mockito.verify(mockHttpClient).executeMethod(captor.capture());

        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals("myUserAgent", sentValue);
    }

}
