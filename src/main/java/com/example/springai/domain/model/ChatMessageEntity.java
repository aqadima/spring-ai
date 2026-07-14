package com.example.springai.domain.model;

import com.example.springai.constants.Constants;
import com.example.springai.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
        name = Constants.Db.MESSAGE_TABLE
)
public class ChatMessageEntity extends BaseEntity<ChatMessageEntity> {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatEntity chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "content", nullable = false)
    private String content;

}
