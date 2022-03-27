package org.pilot.content.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pilot.content.dto.ContentMatcherRequest;
import org.pilot.content.service.IContentMatcherService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentMatcherService implements IContentMatcherService {

    private static final String BUCKET_1 = "1:";
    private static final String BUCKET_2 = "2:";
    private static final String BUCKET_3 = "=:";

    @Override
    public String process(final ContentMatcherRequest contentMatcherRequest) {
        final String input1 = Objects.isNull(contentMatcherRequest.getInput1()) ? "" : contentMatcherRequest.getInput1();
        final String input2 = Objects.isNull(contentMatcherRequest.getInput2()) ? "" : contentMatcherRequest.getInput2();
        log.debug("Request received for processing '{}' and '{}'", input1, input2);

        return processOutput(computeFrequency(input1, input2));
    }

    private static String processOutput(final List<CharacterFrequency> letterFrequency) {
        log.info("Algorithm has computed data for {} valid characters", letterFrequency.size());
        final StringBuilder response = new StringBuilder();

        if(CollectionUtils.isEmpty(letterFrequency)) {
            return response.append("No Result").toString();
        }

        log.info("Constructing response for data collected by algorithm");
        letterFrequency.forEach(entry -> {
            response.append(entry.getBucket());
            response.append(StringUtils.repeat(entry.getLetter(), entry.getCount().intValue()));
            response.append("/");
        });

        return response.substring(0, response.length() - 1);
    }

    private static List<CharacterFrequency> computeFrequency(final String input1, final String input2) {
        final List<CharacterFrequency> letterFrequency = new ArrayList<>();

        log.info("Filtering character from input to retain [a-z]");
        final String filteredInput1 = input1.replaceAll("[^a-z]", "");
        final String filteredInput2 = input2.replaceAll("[^a-z]", "");

        final String characterRange = filteredInput1 + filteredInput2;
        final Set<Character> charsSet = characterRange.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toCollection(HashSet::new));

        log.info("There are {} characters to scan for processing algorithm", charsSet.size());

        charsSet.forEach(character -> {
            long charCountInS1 = input1.chars().filter(ch -> ch == character).count();
            long charCountInS2 = input2.chars().filter(ch -> ch == character).count();

            log.info("Count of '{}' in input-1 is {} and input-2 is {}", character, charCountInS1, charCountInS2);

            if(charCountInS1 > 1 || charCountInS2 > 1) {
                final CharacterFrequency.CharacterFrequencyBuilder entryBuilder = CharacterFrequency.builder();

                if(charCountInS1 > charCountInS2) {
                    entryBuilder.weight(0.3).bucket(BUCKET_1).count(charCountInS1).build();
                } else if(charCountInS1 < charCountInS2) {
                    entryBuilder.weight(0.2).bucket(BUCKET_2).count(charCountInS2).build();
                } else {
                    entryBuilder.weight(0.1).bucket(BUCKET_3).count(charCountInS1).build();
                }

                entryBuilder.letter(character);
                letterFrequency.add(entryBuilder.build());
            }
        });

        letterFrequency.sort(Comparator
                .comparingDouble((CharacterFrequency o) -> o.getWeight() + o.getCount())
                .reversed());
        return letterFrequency;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    private static class CharacterFrequency {
        private Character letter;
        private Double weight;
        private String bucket;
        private Long count;
    }
}