package com.melodyxxx.puredaily.entity.daily;

import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/18.
 * Description:
 */
public class AllComments {

    public Comments longComments;
    public Comments shortComments;

    public AllComments(Comments longComments, Comments shortComments) {
        this.longComments = longComments;
        this.shortComments = shortComments;
    }

    @Override
    public String toString() {
        return "AllComments{" +
                "shortComments=" + shortComments +
                ", longComments=" + longComments +
                '}';
    }

    public class Comments {

        public List<Comment> comments;

        @Override
        public String toString() {
            return "Comments{" +
                    "comments=" + comments +
                    '}';
        }

        public class Comment {

            public String author;
            public String content;
            public String avatar;
            public long time;
            public ReplyTo reply_to;
            public int id;
            public int likes;

            @Override
            public String toString() {
                return "Comment{" +
                        "author='" + author + '\'' +
                        ", content='" + content + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", time=" + time +
                        ", reply_to=" + reply_to +
                        ", id=" + id +
                        ", likes=" + likes +
                        '}';
            }

            public class ReplyTo {
                public String content;
                public int status;
                public int id;
                public String author;

                @Override
                public String toString() {
                    return "ReplyTo{" +
                            "content='" + content + '\'' +
                            ", status=" + status +
                            ", id=" + id +
                            ", author='" + author + '\'' +
                            '}';
                }
            }
        }

    }


}
