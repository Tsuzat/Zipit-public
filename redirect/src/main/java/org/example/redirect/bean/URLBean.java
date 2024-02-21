package org.example.redirect.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * URLBean represents the schema of collection URLBean in database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "URLs")
public class URLBean {

	@Id
	private String id;

	private String original;
	private String shorten;
	private String userId;
	private Date createdOn;
	private Date expiresOn;
}
