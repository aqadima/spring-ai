package com.example.springai.domain.model;

import com.example.springai.constants.Constants;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(
        schema = Constants.Db.APP_SCHEMA,
        name = Constants.Db.CHAT_TABLE
)
public class ChatEntity extends BaseEntity<ChatEntity> {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "max_messages", nullable = false)
    private int maxMessages;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageEntity> messages;

}
