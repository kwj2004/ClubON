-- ClubON department seed data based on the original frontend signup list.
-- WARNING: This script replaces every row in the departments table.
-- Back up the database before running it against a shared or production database.

START TRANSACTION;

DELETE FROM departments;

INSERT INTO departments (name, active, created_at) VALUES
    ('간호학과', TRUE, NOW()),
    ('임상병리학과', TRUE, NOW()),
    ('안경광학과', TRUE, NOW()),
    ('응급구조학과', TRUE, NOW()),
    ('방사선학과', TRUE, NOW()),
    ('치위생학과', TRUE, NOW()),
    ('물리치료학과', TRUE, NOW()),
    ('의료경영학과', TRUE, NOW()),
    ('빅데이터인공지능전공', TRUE, NOW()),
    ('글로벌빅데이터AI학과', TRUE, NOW()),
    ('식품영양전공', TRUE, NOW()),
    ('식품생명공학전공', TRUE, NOW()),
    ('안전공학전공', TRUE, NOW()),
    ('화장품과학전공', TRUE, NOW()),
    ('의료공학전공', TRUE, NOW()),
    ('인문사회계열학부', TRUE, NOW()),
    ('레저산업전공', TRUE, NOW()),
    ('뷰티아트전공', TRUE, NOW()),
    ('시각디자인전공', TRUE, NOW()),
    ('사회복지전공', TRUE, NOW()),
    ('아동청소년상담전공', TRUE, NOW()),
    ('중독상담전공', TRUE, NOW()),
    ('장례산업전공', TRUE, NOW()),
    ('식품영양학과', TRUE, NOW()),
    ('식품산업외식학과', TRUE, NOW()),
    ('보건환경안전학과', TRUE, NOW()),
    ('의료IT학과', TRUE, NOW()),
    ('의료공학과', TRUE, NOW()),
    ('미용화장품과학과', TRUE, NOW()),
    ('의료홍보디자인학과', TRUE, NOW()),
    ('스포츠아웃도어학과', TRUE, NOW()),
    ('장례지도학과', TRUE, NOW()),
    ('중독재활복지학과', TRUE, NOW()),
    ('유아교육학과', TRUE, NOW()),
    ('아동학과', TRUE, NOW()),
    ('빅데이터의료융합학과', TRUE, NOW());

COMMIT;

SELECT id, name, active, created_at
FROM departments
ORDER BY name ASC;
