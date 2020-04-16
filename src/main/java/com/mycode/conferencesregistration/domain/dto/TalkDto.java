package com.mycode.conferencesregistration.domain.dto;

import com.mycode.conferencesregistration.domain.TypeTalk;
import lombok.Value;

/**
 * @author Yurii Kovtun
 */
@Value
public class TalkDto {

    Long id;
    String name;
    String description;
    TypeTalk typeTalk;
    String reporter;
}
