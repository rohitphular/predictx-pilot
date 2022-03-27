package org.pilot.content.service;

import org.pilot.content.dto.ContentMatcherRequest;

public interface IContentMatcherService {

    String process(final ContentMatcherRequest contentMatcherRequest);

}