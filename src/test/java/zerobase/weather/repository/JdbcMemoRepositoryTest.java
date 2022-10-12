package zerobase.weather.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcMemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    @Transactional
    void testJdbc() {
        Memo memo = new Memo();
        memo.setText("test1");
        jdbcMemoRepository.save(memo);

        List<Memo> list = jdbcMemoRepository.findAll();
        for (Memo memo1 : list) {
            System.out.println("memo1 = " + memo1.getId());
            System.out.println("memo1 = " + memo1.getText());
        }


        assertThat(list.size()).isEqualTo(1);
    }



    @Test
    @Transactional
    void findTest() {
        //given
        Memo memo = new Memo();
        memo.setText("memo1");
        memoRepository.save(memo);

        //when
        List<Memo> list = memoRepository.findAll();

        //then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void findByIdTest() {

        //given
        Memo memo = new Memo(99L, "findByIdTest");
        Memo saveMemo = memoRepository.save(memo);
        Long id = saveMemo.getId();

        //when
        Optional<Memo> findId = memoRepository.findById(saveMemo.getId());

        //then
        assertThat(findId.get().getId()).isEqualTo(id);
    }
}