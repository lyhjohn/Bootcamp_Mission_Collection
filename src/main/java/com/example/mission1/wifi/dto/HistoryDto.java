package com.example.mission1.wifi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HistoryDto {

    int ID;
    String X;
    String Y;
    String registeredAt;
    List<HistoryDto> historyList = new ArrayList<>();

}
