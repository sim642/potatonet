SET SCHEMA PUBLIC/;

SET DATABASE EVENT LOG SQL LEVEL 4/;

DROP VIEW V_FEED IF EXISTS/;

CREATE VIEW V_FEED AS
SELECT POST.*, TBL_FRIENDS.PERSON_ID AS FEED_USER_ID
FROM POST
JOIN TBL_FRIENDS ON POST.USER_ID=TBL_FRIENDS.FRIEND_ID
UNION
SELECT POST.*, POST.USER_ID AS FEED_USER_ID
FROM POST
ORDER BY CREATION_DATE_TIME DESC/;


DROP VIEW V_USER_FEED IF EXISTS/;

CREATE VIEW V_USER_FEED AS
SELECT POST.*, POST.USER_ID AS FEED_USER_ID
FROM POST
ORDER BY CREATION_DATE_TIME DESC/;


DROP PROCEDURE TOGGLE_LIKE IF EXISTS/;
DROP PROCEDURE ADD_LIKE IF EXISTS/;
DROP PROCEDURE REMOVE_LIKE IF EXISTS/;

CREATE PROCEDURE  ADD_LIKE(added_post_id BIGINT, added_user_id BIGINT)
  MODIFIES SQL DATA
  INSERT INTO POST_LIKE(post_id, user_id) VALUES (added_post_id, added_user_id)/;

CREATE PROCEDURE REMOVE_LIKE(removed_post_id BIGINT, removed_user_id BIGINT)
  MODIFIES SQL DATA
  DELETE FROM POST_LIKE WHERE post_id=removed_post_id AND user_id=removed_user_id/;

CREATE PROCEDURE TOGGLE_LIKE(arg_post_id BIGINT, arg_user_id BIGINT)
  MODIFIES SQL DATA
  BEGIN ATOMIC
    IF EXISTS (SELECT * FROM POST_LIKE WHERE POST_LIKE.post_id = arg_post_id AND POST_LIKE.user_id = arg_user_id)
    THEN
      CALL REMOVE_LIKE(arg_post_id, arg_user_id);
    ELSE
      CALL ADD_LIKE(arg_post_id, arg_user_id);
    END IF;
    UPDATE POST SET like_count = (SELECT COUNT(user_id) FROM POST_LIKE WHERE post_id=arg_post_id) WHERE id=arg_post_id;
  END/;