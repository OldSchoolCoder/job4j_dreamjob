select * from candidate;
alter sequence candidate_id_seq restart with 1;
alter sequence photo_id_seq restart with 1;
delete from candidate;
delete from photo;
delete from p