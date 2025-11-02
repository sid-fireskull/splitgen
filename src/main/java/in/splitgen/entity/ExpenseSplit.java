package in.splitgen.entity;

import in.splitgen.constant.SplitType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Defines a participant's share of a specific expense.
 * Corresponds to the 'expense_split' table.
 */
@Entity
@Table(name = "expense_split", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"expense_id", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "share_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal shareAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_method", nullable = false)
    private SplitType splitMethod = SplitType.EQUAL;
}

