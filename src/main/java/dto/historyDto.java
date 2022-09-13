package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class historyDto {

    int ID;
    String X;
    String Y;
    String registeredAt;
    List<historyDto> historyList = new ArrayList<>();

}
