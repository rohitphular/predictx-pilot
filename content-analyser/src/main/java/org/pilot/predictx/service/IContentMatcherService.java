package org.pilot.predictx.service;

import org.pilot.predictx.dto.ContentMatcherRequest;

public interface IContentMatcherService {

    String process(final ContentMatcherRequest contentMatcherRequest);

}