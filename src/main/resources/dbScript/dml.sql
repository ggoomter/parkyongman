#등급
INSERT INTO parkyongman.tb_grade
(id, `order`, name)
VALUES(1, 1, '일반회원');

#유저
INSERT INTO parkyongman.tb_user
(id, pw, name, email, phone, join_date, last_login, exit_yn, grade_id)
VALUES('ggoomter', '0070', '배성원', 'ggoomter@gmail.com', '01026585834', now(), now(), 'N', 1);

#게시판
INSERT INTO parkyongman.tb_board
(upper_board_idx, name, def, show_yn, creator, modifier, create_time, update_time)
VALUES(null, '자유게시판', '누구나 일기가능, 쓰기는 일반회원이상', 'Y', 'ggoomter', 'ggoomter', now(), now());


#글
INSERT INTO parkyongman.tb_post
(board_idx, title, content, view_cnt, notice_yn, secret_yn, delete_yn, writer, insert_time, update_time, delete_time)
VALUES(0, '', '', 0, 'N', 'N', 'N', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '');


select * from tb_board tb ;
select * from tb_post tp ;