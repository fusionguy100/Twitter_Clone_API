package com.cooksys.social_media.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
public class ProfileDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
