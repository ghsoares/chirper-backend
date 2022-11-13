package com.ghsoares.chirper.model;

import java.time.LocalDate;
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

@Entity
@Table(name="tb_chirp")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate creationDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate editDate;
	
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

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public LocalDate getEditDate() {
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

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public void setEditDate(LocalDate editDate) {
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





