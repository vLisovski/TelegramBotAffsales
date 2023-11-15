package com.lisovski.tgspringbot.dtos;

import com.lisovski.tgspringbot.models.Flow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFlowsAnswer {
    private Map<String, Flow> offerThreadsMap;
}
