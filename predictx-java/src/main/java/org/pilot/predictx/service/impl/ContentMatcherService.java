package org.pilot.predictx.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.pilot.predictx.dto.ContentMatcherRequest;
import org.pilot.predictx.service.IContentMatcherService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentMatcherService implements IContentMatcherService {

    @Override
    public String process(final ContentMatcherRequest contentMatcherRequest) {
        final String input1 = Objects.isNull(contentMatcherRequest.getInput1()) ? "" : contentMatcherRequest.getInput1();
        final String input2 = Objects.isNull(contentMatcherRequest.getInput2()) ? "" : contentMatcherRequest.getInput2();

        return apply(computeFrequency(input1, input2));
    }

    private static String apply(final List<CharacterFrequency> letterFrequency) {
        final StringBuilder response = new StringBuilder();

        if(CollectionUtils.isEmpty(letterFrequency)) {
            return response.append("No Result").toString();
        }

        letterFrequency.forEach(entry -> {
            response.append(entry.getBucket());
            response.append(StringUtils.repeat(entry.getLetter(), entry.getCount().intValue()));
            response.append("/");
        });

        return response.substring(0, response.length() - 1);
    }

    private static List<CharacterFrequency> computeFrequency(final String input1, final String input2) {

        final List<CharacterFrequency> letterFrequency = new ArrayList<>();

        final String filteredInput1 = input1.replaceAll("[^a-z]", "");
        final String filteredInput2 = input2.replaceAll("[^a-z]", "");

        final String characterRange = filteredInput1 + filteredInput2;
        final Set<Character> charsSet = characterRange.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toCollection(HashSet::new));

        charsSet.forEach(character -> {
            long charCountInS1 = input1.chars().filter(ch -> ch == character).count();
            long charCountInS2 = input2.chars().filter(ch -> ch == character).count();

            if(charCountInS1 > 1 || charCountInS2 > 1) {
                final CharacterFrequency.CharacterFrequencyBuilder entryBuilder = CharacterFrequency.builder();

                if(charCountInS1 > charCountInS2) {
                    entryBuilder.weight(0.3).bucket("1:").count(charCountInS1).build();
                } else if(charCountInS1 < charCountInS2) {
                    entryBuilder.weight(0.2).bucket("2:").count(charCountInS2).build();
                } else {
                    entryBuilder.weight(0.1).bucket("=:").count(charCountInS1).build();
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