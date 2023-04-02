package kr.codesqaud.cafe.repository;

import kr.codesqaud.cafe.domain.Answer;
import kr.codesqaud.cafe.dto.answer.AnswerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:db/schema.sql")
@Sql("classpath:db/data.sql")
class H2AnswerRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    ArticleRepository repository;
    MemberRepository memberRepository;
    Answer answer;

    static final String TEST_CONTENTS = "test 댓글";

    @BeforeEach
    void init() {
        repository = new H2ArticleRepository(jdbcTemplate.getDataSource());
        memberRepository = new H2MemberRepository(jdbcTemplate.getDataSource());

        answer = new Answer();
        answer.setArticleId(1L);
        answer.setContents(TEST_CONTENTS);
        answer.setWriter(memberRepository.findById(1L));
    }

    @Test
    @DisplayName("모든 댓글을 조회할 수 있다.")
    void findAll() {
        assertThat(repository.findReplyByArticleId(0L)).isEmpty();
    }

    @Test
    @DisplayName("댓글을 1개 저장하면 리스트 크기가 1 늘어나야한다.")
    void save() {
        int before = repository.findReplyByArticleId(1L).size();
        repository.saveReply(answer);
        assertThat(repository.findReplyByArticleId(1L)).hasSize(before + 1);
    }

    @Test
    @DisplayName("댓글 ID로 내용을 읽어올 수 있다.")
    void findById() {
        repository.saveReply(answer);
        assertThat(repository.findReplyById(1L).getContents()).isEqualTo(TEST_CONTENTS);
    }

    @Test
    @DisplayName("댓글 저장 후 작성자의 이름을 확인할 수 있다.")
    void saveCheckDataWriter() {
        int before = repository.findReplyByArticleId(1L).size();
        repository.saveReply(answer);
        assertThat(repository.findReplyById(1L).getNickname()).isEqualTo("자바지기");
    }

    @Test
    @DisplayName("댓글을 수정하면 댓글 contents가 바뀌어야 한다.")
    void update() {
        repository.saveReply(answer);
        AnswerResponseDto exAnswer = repository.findReplyById(1L);
        Answer newAnswer = new Answer();
        newAnswer.setContents("수정된 테스트 댓글 내용");
        repository.updateReply(exAnswer.getAnswerIndex(), newAnswer.getContents());
        assertThat(repository.findReplyById(1L).getContents()).isEqualTo("수정된 테스트 댓글 내용");
    }

    @Test
    @DisplayName("댓글을 1 삭제하면 총 댓글 개수가 1 줄어야 한다.")
    void delete() {
        repository.saveReply(answer);
        int size = repository.findReplyByArticleId(1L).size();
        repository.deleteAReply(1L);
        assertThat(repository.findReplyByArticleId(1L)).hasSize(size-1);
    }

}
