package com.cooksys.social_media.dtos;

import com.cooksys.social_media.entities.embeddables.Credentials;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class CredentialsDto {

    private String username;

    private String password;


}
