package kscience.kmath.commons.optimization

import kscience.kmath.expressions.DifferentiableExpression
import kscience.kmath.expressions.Expression
import kscience.kmath.expressions.Symbol

public interface OptimizationResultFeature


public class OptimizationResult<T>(
    public val point: Map<Symbol, T>,
    public val value: T,
    public val features: Set<OptimizationResultFeature> = emptySet(),
)

/**
 * A configuration builder for optimization problem
 */
public interface OptimizationProblem<T : Any> {
    /**
     * Set an objective function expression
     */
    public fun expression(expression: Expression<Double>): Unit

    /**
     *
     */
    public fun diffExpression(expression: DifferentiableExpression<Double>): Unit
    public fun update(result: OptimizationResult<T>)
    public fun optimize(): OptimizationResult<T>
}
