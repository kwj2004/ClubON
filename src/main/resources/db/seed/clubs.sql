-- ClubON presentation/frontend seed data.
-- Run this file manually in MySQL Workbench after the Spring Boot app has created/updated the schema.
-- The INSERT statements are written to avoid duplicate rows by club name.

INSERT INTO clubs (
    name,
    type,
    category,
    short_description,
    full_description,
    recruit_period,
    recruit_condition,
    activity_info,
    contact_url,
    status,
    image_url,
    created_at
)
SELECT
    '멋쟁이사자처럼',
    'CENTRAL',
    'ETC',
    '국내 최대 규모 IT 연합 개발 동아리',
    '멋쟁이사자처럼 을지대학교는 13년의 전통을 가진 국내 최대 규모 IT 연합 동아리입니다. 매년 아이디어톤, 해커톤 등 다양한 행사를 진행하며, 웹 개발, 앱 개발, 기획, 디자인, 창업에 관심 있는 학생들이 함께 아이디어를 실현하는 활동을 합니다.',
    '2025.02.19 ~ 2025.03.09',
    '웹 개발 및 앱 개발에 관심 있거나, 아이디어를 실현시키려는 열정을 가진 을지대학교 재학생',
    '개발트랙(프론트엔드, 백엔드), 기획&디자인 트랙 / 13기 활동기간: 2025.03 ~ 2025.12',
    'https://forms.gle/b8nKmRo52nyHnXsL9',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '멋쟁이사자처럼');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    'DNG',
    'CENTRAL',
    'CULTURE_ART',
    '을지대학교 성남캠퍼스 중앙댄스동아리',
    'DNG는 전공 여부와 무관하게 춤에 관심이 있는 을지대학교 재학생이라면 누구나 함께할 수 있는 중앙댄스동아리입니다. 학번과 학년 제한 없이 함께 연습하고 공연을 준비하며, 공연 준비 기간에는 많은 시간이 투자됩니다. 시험기간에는 부원들의 학습을 위해 연습을 진행하지 않습니다.',
    '2026.02.26 ~ 2026.03.07',
    '춤에 관심 있는 을지대학교 재학생 누구나 지원 가능, 학번·학년 제한 없음',
    '정기 연습: 매주 월요일, 목요일 / 오디션: 2026.03.12 18시 이후, 뉴밀레니엄 지하 1층 연습실 / 결과 발표: 2026.03.13 13:00 문자 발송',
    'https://forms.gle/X2K6c4peKuaxghA59',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = 'DNG');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '새밝소리',
    'CENTRAL',
    'CULTURE_ART',
    '을지대학교 풍물패 중앙동아리',
    '새밝소리는 깡과 의리로 뭉친 을지대학교 풍물패 중앙동아리입니다. 꽹과리, 장구, 북 등의 악기를 함께 배우고 연습하며, 신입생과 재학생 모두 부담 없이 참여할 수 있습니다. 면접 없이 구글폼 신청으로 지원할 수 있습니다.',
    '2026.03.29까지',
    '성남캠퍼스 재학생 대상, 신입생 및 재학생 모두 지원 가능',
    '월요일 단체 연습, 수요일 악기별 연습 / 2026년 주요 활동: 4월 벚꽃축제 공연, 신입생 환영회, 7월 여름 모꼬지, 8월 여름 전수, 9월 개강굿, 10월 축제 공연, 11월 정기공연, 1월 겨울 전수 / 입회비 25,000원, 월 회비 7,000원',
    'https://docs.google.com/forms/d/e/1FAIpQLSd-AQSxUyUbE6XL_Zly8GmCoZgmA8du3qyXCoQjktiIeh_w9A/viewform?usp=publish-editor',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '새밝소리');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    'LUNATIC+',
    'CENTRAL',
    'CULTURE_ART',
    '을지대학교 연극/뮤지컬 중앙동아리',
    'LUNATIC+는 을지대학교 연극/뮤지컬 중앙동아리입니다. 무대 위 배우뿐 아니라 무대세트 제작, 무대 스탭, 메이크업, 음향, 조명, 홍보부, 총무부 등 다양한 분야의 부원이 함께 공연을 만들어갑니다. 모든 부원이 아마추어로 구성되어 있어 연극 경험이 없어도 관심과 열정만 있다면 누구나 함께할 수 있습니다. 2022년 탑과 그림자, 2023년 유린타운, 2024년 Fame, 2025년 빨래 공연을 성공적으로 마쳤으며, 2026년에도 새로운 작품을 준비합니다.',
    '2026.02.20 ~ 2026.03.12 20:00',
    '연극과 뮤지컬에 관심 있는 을지대학교 재학생 누구나 지원 가능, 중복지원 가능',
    '모집 분야: 배우, 무대세트 제작 및 무대 스탭, 메이크업, 음향, 조명, 홍보부, 총무부 / 메인 활동: 9월 말 정기공연 / 기타 활동: 여름 MT, 뮤지컬 관람 등 부원 교류 행사 / 면접: 2026.03.13, 2026.03.16 예정, 창의관 지하 1층 루나틱 동아리방 / 시험기간에는 연습 미진행',
    'https://forms.gle/p9R8KLcpdPwo9Zf17',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = 'LUNATIC+');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    'CAM',
    'CENTRAL',
    'RELIGION',
    '을지대학교 중앙 기독교 동아리',
    'CAM은 캠퍼스 안에서 함께 예배드리고 삶을 나누며 믿음으로 성장하는 을지대학교 중앙 기독교 동아리입니다. Christ’s Ambassador Mission, 그리스도의 대사라는 의미를 가진 초교파 캠퍼스 선교단체로 전국 여러 대학에서 함께 활동하고 있습니다. 예배 공동체를 찾는 학생, 믿음의 친구를 만나고 싶은 학생, 하나님을 더 알고 싶은 학생은 물론 처음 한 번 참여해보고 싶은 학생도 부담 없이 함께할 수 있습니다.',
    NULL,
    '신입생이 아니어도 지원 가능, 교회를 다니지 않아도 참여 가능, 회비 없음',
    '주 1회 정기예배, 예배 후 무료 식사교제, 양육 모임, 보드게임·볼링·맛집 탐방 등 친목모임, 여름·겨울 정기 수련회, 국내·해외 선교 활동',
    'https://naver.me/GntrGuAs',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = 'CAM');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '호크',
    'CENTRAL',
    'SPORTS',
    '을지대학교 농구 중앙동아리',
    '호크(Hawk)는 농구를 좋아하거나 사랑하는 학생들이 함께 즐겁게 농구하는 을지대학교 농구 중앙동아리입니다. 농구를 잘하지 않아도 배우고 싶은 마음이 있다면 함께할 수 있으며, 신입 부원뿐 아니라 동아리 운영을 함께할 매니저도 모집합니다. 올해 대학부 대회팀을 준비 중이며, 친목활동과 단체 MT, 농구경기 단체직관 등 다양한 활동도 계획하고 있습니다.',
    NULL,
    '남녀 불문 농구를 좋아하거나 배우고 싶은 을지대학교 재학생, 매니저는 농구를 몰라도 지원 가능',
    '정기 활동: 주 3회 월·수·금 18:00~21:00, 수요일은 19:00~21:00로 변동 가능 / 장소: 지천관 / 매니저 활동: SNS 운영, 홍보, 영상 촬영 및 편집 / 활동: 대학부 대회팀 준비, 단체직관, 단체 MT, 타 학교 농구팀 및 사회인 팀과 친선경기',
    'https://naver.me/5ECxBApV',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '호크');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    'F.L.A.S.H',
    'GENERAL',
    'CULTURE_ART',
    '을지대학교 사진동아리',
    'F.L.A.S.H는 Focus, Light, Art, Story, Horizon의 의미를 담은 을지대학교 사진동아리입니다. 반짝이는 찰나의 순간을 사진으로 기록하며, 단 한 번뿐인 대학 생활의 순간들을 소중한 추억으로 남기는 활동을 합니다. DSLR 카메라가 없거나 사진에 익숙하지 않아도 사진에 대한 열정만 있다면 누구나 함께할 수 있으며, 사진 모델을 하고 싶은 학생도 지원할 수 있습니다.',
    '2026.02.28 ~ 2026.03.06 18:00',
    '을지대학교 재학생 누구나 지원 가능, 사진에 관심이 있거나 사진 찍는 것을 좋아하는 학생, 사진에 대해 함께 이야기하고 싶은 학생, 사진 찍을 기회를 찾고 있는 학생',
    '정기활동: 매달 한 번 출사 활동, 시험 기간에는 출사 변경 가능 / 주요활동: 사진 용어, 구도, 보정 방법 학습, 서울·경기 지역 출사 한 학기 2~3회, 교내·온라인 전시, 전시회 방문 등 / 모집인원: 최대 20명',
    'https://forms.gle/RAqKQ6ZJSsAMAjVH7',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = 'F.L.A.S.H');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '야구의 숲',
    'GENERAL',
    'SPORTS',
    '을지대학교 야구 직관 동아리',
    '야구의 숲은 야구를 좋아하는 을지대학교 학우들이 함께 직관을 가고, 응원하고, 친목을 쌓는 야구 직관 동아리입니다. 모토는 자유이며, 직관 번개, 모임, 활동 모두 자율 참여 방식으로 운영됩니다. 같이 직관 갈 사람이 없었거나 같은 팀 팬을 학교에서 찾고 싶었던 학생들이 편하게 함께할 수 있습니다.',
    '2026.03.12 24:00까지',
    '야구를 좋아하는 을지대학교 재학생 누구나 지원 가능, 면접 없이 바로 참여 가능',
    '주요 활동: 야구 직관, 동아리방 단체 경기 관람, 친목 활동 및 회식, 방학 MT, 지방 구단 직관, 단체 관람 선예매 기회 / 동아리방 빔프로젝터 구입 예정',
    'https://forms.gle/XgK6dy7L6hwihtAz5',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '야구의 숲');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '오리자',
    'CENTRAL',
    'VOLUNTEER',
    '을지대학교 성남캠퍼스 중앙 봉사동아리',
    '오리자는 1967년에 설립되어 60년의 전통을 이어오고 있는 을지대학교 성남캠퍼스 중앙 봉사동아리입니다. 봉사시간도 채우고 좋은 인연도 만들며, 보람 있는 대학생활을 함께 만들어가는 동아리입니다. 다양한 기관과 연계하여 의미 있고 즐거운 봉사활동을 기획하고 있습니다.',
    '2026.02.26 ~ 2026.03.10',
    '신입생, 편입생, 재학생 모두 지원 가능 / 졸업요건 봉사시간을 채우고 싶은 학생, 다양한 봉사활동을 경험하고 싶은 학생, 타과 친구 및 선후배들과 교류하고 싶은 학생',
    '주요 활동: 명일역 해뜨는집 집수리 봉사, 청소년센터 연계 봉사, 지역행사 보조 및 부스 운영, 아이돌봄 봉사활동, 청소년 요리활동 보조 / 봉사는 주로 주말 진행, 활동 일정은 보통 전월 말 공지, 모든 활동 참여는 자율 / 가입비 10,000원',
    'https://naver.me/GYDv7QiW',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '오리자');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '소낙비',
    'GENERAL',
    'ETC',
    '알고리즘 문제풀이와 자유 프로젝트를 함께하는 PS 동아리',
    '소낙비는 알고리즘 문제풀이(PS)를 함께하고 싶은 학생들이 모여 자유롭게 운영되는 일반동아리입니다. 정기 출석과 회비 없이 디스코드 기반으로 각자 문제를 풀고, 필요할 때 서로 도움을 주는 방식으로 운영됩니다. 백준 문제풀이, 풀이 공유, 코딩테스트 대비, 실력 향상 목적의 참여가 가능하며 AI, 구현 등 개인 프로젝트도 환영합니다.',
    NULL,
    '알고리즘 문제풀이, 코딩테스트 대비, 개발 실력 향상, 개인 프로젝트에 관심 있는 을지대학교 재학생 누구나 참여 가능',
    '디스코드 기반 자유 운영 / 백준 문제풀이 도움 및 풀이 공유 / 개인 프로젝트 환영 / 간단한 기술 세미나 비정기 진행 / 자유 프로젝트 채팅 개설 가능 / 정기 출석 없음, 회비 없음',
    'https://discord.gg/SvW5qggHnN',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '소낙비');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    'IPPD',
    'GENERAL',
    'ETC',
    '심리극 기법을 학습하고 체험하는 학술·자기탐색 동아리',
    'IPPD(Inner side Portrayal Psychology Drama)는 심리극 기법을 활용해 자신의 내면을 탐색하고 타인과 깊은 교감을 나누는 일반동아리입니다. 공연과 연출 중심의 연극 동아리가 아니라, 사이코드라마 기법을 학습하고 체험하는 학술·자기탐색 중심 동아리입니다. 2026년에는 사이코드라마의 기본 원리를 배우고 다양한 내면 묘사 활동을 통해 나를 찾는 여정을 함께합니다.',
    '2026.03.03 ~ 2026.03.20',
    '주전공이 사회복지 혹은 중독상담인 을지대학교 재학생',
    '주요 활동: 사이코드라마 기본 원리 학습, 심리극 기법 체험, 내면 묘사 활동, 감정 그래프, 심리극 관람, 정보 공유 멘토링, 회식 및 뒤풀이, 심화 프로그램, 참여아이템경진대회 참여 기회 / 월 1~2회 만남 / 4월 중순 첫 활동 예정 / 성남캠퍼스 중심 진행',
    'https://docs.google.com/forms/d/e/1FAIpQLScppaG33EKkd-0vJmj7cJ9V8Wmk4-RhRvO9AQVOfGx3KL8ZrA/viewform?usp=header',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = 'IPPD');

INSERT INTO clubs (
    name, type, category, short_description, full_description, recruit_period,
    recruit_condition, activity_info, contact_url, status, image_url, created_at
)
SELECT
    '오션홀릭',
    'GENERAL',
    'SPORTS',
    '을지대학교 스쿠버 다이빙 동아리',
    '오션홀릭은 을지대학교 스쿠버 다이빙 동아리입니다. 다이빙 자격증 취득이 가능하며, 외부보다 저렴한 비용으로 스쿠버 다이빙을 배울 수 있습니다. 수영을 못해도 공기통을 메고 물속에서 호흡하며 활동하는 스포츠이기 때문에 부담 없이 도전할 수 있습니다. 다이빙 투어뿐 아니라 물놀이, 워터파크, 겨울 스키장 등 다양한 활동도 함께 즐깁니다.',
    '2026.03.06까지',
    '을지대학교 성남캠퍼스 재학생, 신입생, 편입생, 복학생 지원 가능',
    '주요 활동: 스쿠버 다이빙 교육 및 자격증 취득, 바다 다이빙 투어, 빠지·워터파크 등 물놀이, 겨울 스키장 활동 / 1차 서류 합격 및 면접 공지: 2026.03.09 카카오워크 공지 예정 / 2차 면접: 2026.03.16 18:30부터',
    'https://buly.kr/uViPPe',
    'OPEN',
    NULL,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE name = '오션홀릭');

