package com.redhat.lightblue.camel.utils;

import org.junit.Test;
import org.mockito.Mockito;

import com.redhat.lightblue.client.response.LightblueErrorResponseException;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestLightblueErrorVerifier {

    /**
     * asserts true if no exception is thrown.
     */
    @Test
    public void verify_true() {
        LightblueResponse response = Mockito.mock(LightblueResponse.class);
        Mockito.when(response.hasError()).thenReturn(false);

        LightblueErrorVerifier verifier = new LightblueErrorVerifier();
        verifier.verify(response);
    }

    @Test(expected = LightblueErrorResponseException.class)
    public void verify_false() {
        LightblueResponse response = Mockito.mock(LightblueResponse.class);
        Mockito.when(response.hasError()).thenReturn(true);

        LightblueErrorVerifier verifier = new LightblueErrorVerifier();
        verifier.verify(response);
    }

}
