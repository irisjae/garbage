package garbage;

import java.io.Serializable;
import java.util.UUID;

public class Session implements Serializable {
	public UUID uuid;
	public String loginName;

	public Session (User user) {
		this (user, UUID .randomUUID ()); }
	public Session (User user, UUID uuid) {
		this .uuid = uuid;
		this .loginName = user .loginName; } }
