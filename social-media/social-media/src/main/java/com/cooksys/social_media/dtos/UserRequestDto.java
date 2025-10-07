package com.cooksys.social_media.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
public class UserRequestDto {

    private CredentialsDto credentials;

    private ProfileDto profile;



}
