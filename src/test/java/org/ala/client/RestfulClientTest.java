package org.ala.client;

import org.ala.client.appender.RestfulAppender;
import org.ala.client.model.LogEventVO;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RestfulClient.class, RestfulAppender.class})
public class RestfulClientTest {

    @Test
    public void testUserAgentSetToUndefinedIfNotProvidedInTheContext() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.getParams()).thenReturn(mock(HttpClientParams.class));
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.getDeserializationConfig()).thenReturn(mock(DeserializationConfig.class));
        when(mockMapper.getSerializationConfig()).thenReturn(mock(SerializationConfig.class));
        whenNew(ObjectMapper.class).withAnyArguments().thenReturn(mockMapper);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        RestfulAppender appender = new RestfulAppender();
        appender.setUrlTemplate("someurl");
        LoggingEvent event = mock(LoggingEvent.class);
        when(event.getMessage()).thenReturn(message);
        appender.doAppend(event);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        verify(mockHttpClient).executeMethod(captor.capture());

        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals(Constants.UNDEFINED_USER_AGENT_VALUE, sentValue);
    }

    @Test
    public void testUserAgentSetToValueProvidedInContext() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.getParams()).thenReturn(mock(HttpClientParams.class));
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.getDeserializationConfig()).thenReturn(mock(DeserializationConfig.class));
        when(mockMapper.getSerializationConfig()).thenReturn(mock(SerializationConfig.class));
        whenNew(ObjectMapper.class).withAnyArguments().thenReturn(mockMapper);

        String message = "{\"eventTypeId\": 1,\"comment\": \"For doing some research with..\",\"userEmail\" : \"waiman.mok@csiro.au\",\"userIP\" : \"123.11.01.112\",\"recordCounts\" : {\"dp123\": 32,\"dr143\": 22,\"ins322\": 55 } }";

        RestfulAppender appender = new RestfulAppender();
        appender.setUrlTemplate("somurl");
        LoggingEvent event = mock(LoggingEvent.class);
        when(event.getMessage()).thenReturn(message);
        when(event.getMDC(eq(Constants.USER_AGENT_PARAM))).thenReturn("myUserAgent");
        appender.doAppend(event);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        verify(mockHttpClient).executeMethod(captor.capture());

        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals("myUserAgent", sentValue);
    }

    @Test
    public void testUserAgentSetToValueProvidedEvent() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.getParams()).thenReturn(mock(HttpClientParams.class));
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);

        LogEventVO logEventVO = new LogEventVO(1, 1, 1, "userEmail", "For doing some research with..", "123.11.01.112", "myUserAgent", new HashMap<String, Integer>() {{
            put("dp123", 32);
            put("dr143", 22);
            put("ins322", 55);
        }});

        RestfulAppender appender = new RestfulAppender();
        appender.setUrlTemplate("somurl");
        LoggingEvent event = mock(LoggingEvent.class);
        when(event.getMessage()).thenReturn(logEventVO);

//        when(event.getMDC(eq(Constants.USER_AGENT_PARAM))).thenReturn("myUserAgent");
        appender.doAppend(event);

        ArgumentCaptor<HttpMethod> captor = ArgumentCaptor.forClass(HttpMethod.class);
        verify(mockHttpClient).executeMethod(captor.capture());

        String sentValue = captor.getValue().getRequestHeader(Constants.USER_AGENT_PARAM).getValue();
        assertEquals("myUserAgent", sentValue);
    }


}
