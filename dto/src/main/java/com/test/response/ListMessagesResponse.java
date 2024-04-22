package com.test.response;

import com.test.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMessagesResponse {

    private List<MessageDto> messages;
}
