package com.mycode.conferencesregistration.domain.dto;

import com.mycode.conferencesregistration.domain.TypeTalk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yurii Kovtun
 */
@Getter
@AllArgsConstructor
public class TalkCreationRequest {

    @NotEmpty(message = "The talk name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The talk description can not be missed or empty")
    private String description;

    @NotNull(message = "The talk type can not be missed or empty")
    private TypeTalk typeTalk;

    @NotEmpty(message = "The reporter can not be missed or empty")
    private String reporter;
}
