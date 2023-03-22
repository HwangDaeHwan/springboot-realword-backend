package com.realworld.backend.domain.profile.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.realworld.backend.domain.common.entity.BaseEntity;
import com.realworld.backend.domain.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows", uniqueConstraints = {
		@UniqueConstraint(name = "u_follow_followee_pair_must_be_unique", 
				columnNames = {"followee", "follower"})
})
public class FollowEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "followee")
	private UserEntity followee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "follower")
	private UserEntity follower;
}
