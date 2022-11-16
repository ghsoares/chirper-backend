package com.ghsoares.chirper.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Entity
@Table(name="tb_chirp")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
/**
 * @author Gabriel
 * Chirp is the equivalent of a tweet for twitter,
 * it's a single user post that contains a message
 */
public class Chirp {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long chirpId;
	
	@ManyToOne
	@JsonIgnoreProperties({ "chirps", "likes" })
	private User author;
	
	@NotNull(message = "The body can't be null")
	private String body;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime creationDate;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime editDate;
	
	@ElementCollection
	@CollectionTable(name = "chirp_tags")
	private Set<String> tags = new HashSet<String>();
	
	@ManyToOne
	@JsonIgnoreProperties({ "replies" })
	private Chirp replyOf;
	
	@OneToMany(mappedBy = "replyOf", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "replyOf" })
	private List<Chirp> replies = new ArrayList<Chirp>();
	
	@OneToMany(mappedBy = "chirp", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "chirp" })
	private List<ChirpLike> likes = new ArrayList<ChirpLike>();

	public Long getChirpId() {
		return chirpId;
	}

	public User getAuthor() {
		return author;
	}

	public String getBody() {
		return body;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public LocalDateTime getEditDate() {
		return editDate;
	}

	public Set<String> getTags() {
		return tags;
	}

	public Chirp getReplyOf() {
		return replyOf;
	}

	public List<Chirp> getReplies() {
		return replies;
	}

	public List<ChirpLike> getLikes() {
		return likes;
	}

	public void setChirpId(Long chirpId) {
		this.chirpId = chirpId;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public void setEditDate(LocalDateTime editDate) {
		this.editDate = editDate;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public void setReplyOf(Chirp replyOf) {
		this.replyOf = replyOf;
	}

	public void setReplies(List<Chirp> replies) {
		this.replies = replies;
	}

	public void setLikes(List<ChirpLike> likes) {
		this.likes = likes;
	}
}





