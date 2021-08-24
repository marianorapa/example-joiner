package com.example.joiner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage {

    private String uuid;
    private Boolean status;

    public static OutputMessage random() {
        return new OutputMessage(UUID.randomUUID().toString(), new Random().nextBoolean());
    }

}
