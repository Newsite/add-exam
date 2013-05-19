--exams
insert into exams(id, access, description, name,random_order, questions_count, published, show_answers, pass_score, totalTime) values(1, 'PUBLIC', 'Our typical customer is a large enterprise that takes more than 3,000 tests annually. Our cost is around $1 (US) per test taken for these entry-level customers. We do however have many smaller organizations that find Test.com a great value, but take far fewer tests per year.', 'Common addExam.com knowledge', true, 5, true, true, 60, null );

--questions
insert into exam_questions(id, body, explanation, random_order, completed, exam_id) values(1, 'How did you know about addExam.com ?', 'explanation', true, 'SINGLE_ANSWER', 1 );
insert into exam_questions(id, body, explanation, random_order, completed, exam_id) values(2, 'How long would you use our service?', 'explanation', true, 'MULTI_CHOICE', 1 );
insert into exam_questions(id, body, explanation, random_order, completed, exam_id) values(3, 'Are the prices high ?', 'explanation', true, 'SINGLE_ANSWER', 1 );
insert into exam_questions(id, body, explanation, random_order, completed, exam_id) values(4, 'Do you like addExam.com design?', 'explanation', true, 'SINGLE_ANSWER', 1 );
insert into exam_questions(id, body, explanation, random_order, completed, exam_id) values(5, 'How do you think is it fast?', 'explanation', true, 'SINGLE_ANSWER', 1 );


--answers
insert into exam_question_answers values(1, 'From Google', true, 1);
insert into exam_question_answers values(2, 'From Social Networks', false, 1);
insert into exam_question_answers values(3, 'From Friends', false, 1);
insert into exam_question_answers values(4, 'From blogs', false, 1);

insert into exam_question_answers values(5, 'Never', true, 2);
insert into exam_question_answers values(6, '1 month', false, 2);
insert into exam_question_answers values(7, '1 year', false, 2);
insert into exam_question_answers values(8, 'many years', false, 2);

insert into exam_question_answers values(9, 'yes', true, 3);
insert into exam_question_answers values(10, 'no', false, 3);

insert into exam_question_answers values(11, 'yes', true, 4);
insert into exam_question_answers values(12, 'no', false, 4);

insert into exam_question_answers values(13, 'yes', true, 5);
insert into exam_question_answers values(14, 'no', false, 5);