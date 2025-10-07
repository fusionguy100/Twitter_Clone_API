package com.cooksys.social_media.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@Data
public class UserResponseDto {
    private String username;

    private ProfileDto profile;

    private Timestamp joined;



}
