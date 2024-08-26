package com.pawalert.backend.global;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImageInfo {

    private Long imageUserId;
    private String imageUrl;
    private boolean isDelete = false;

}
