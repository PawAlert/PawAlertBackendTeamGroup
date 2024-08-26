package com.pawalert.backend.domain.mypet.model;

import java.util.List;

public record PetViewListRequest(
        List<Long> petIdList
) {
}
