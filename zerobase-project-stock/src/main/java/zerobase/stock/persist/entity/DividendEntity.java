package zerobase.stock.persist.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.stock.model.Dividend;

@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = { //복합유니크키: 두 값이 동시에 중복되게 저장 불가능
		@UniqueConstraint(columnNames = {"companyId", "date"})}
)
public class DividendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long companyId;
	private LocalDateTime date;
	private String dividend;

	public DividendEntity(Long companyId, Dividend dividend) {
		this.companyId = companyId;
		this.date = dividend.getDate();
		this.dividend = dividend.getDividend();
	}
}
