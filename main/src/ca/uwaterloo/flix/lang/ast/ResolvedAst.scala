package ca.uwaterloo.flix.lang.ast

trait ResolvedAst

object ResolvedAst {

  case class Root(
                   // todo: type environment???
                   constants: Map[Name.Resolved, ResolvedAst.Definition.Constant],
                   enums: Map[Name.Resolved, ResolvedAst.Definition.Enum],
                   lattices: Map[ResolvedAst.Type, ResolvedAst.Definition.Lattice],
                   relations: Map[Name.Resolved, ResolvedAst.Definition.Relation],
                   facts: List[ResolvedAst.Constraint.Fact],
                   rules: List[ResolvedAst.Constraint.Rule]) extends ResolvedAst

  sealed trait Definition

  object Definition {

    case class Constant(name: Name.Resolved, exp: ResolvedAst.Expression, tpe: ResolvedAst.Type) extends ResolvedAst.Definition

    case class Enum(name: Name.Resolved, cases: Map[String, ResolvedAst.Type.Tag]) extends ResolvedAst.Definition

    case class Lattice() extends ResolvedAst.Definition

    case class Relation() extends ResolvedAst.Definition

  }

  sealed trait Constraint extends ResolvedAst

  object Constraint {

    /**
     * A resolved AST node representing a fact declaration.
     *
     * @param head the head predicate.
     */
    case class Fact(head: ResolvedAst.Predicate.Head) extends ResolvedAst.Constraint

    /**
     * A resolved AST node representing a rule declaration.
     *
     * @param head the head predicate.
     * @param body the body predicates.
     */
    case class Rule(head: ResolvedAst.Predicate.Head, body: List[ResolvedAst.Predicate.Body]) extends ResolvedAst.Constraint

  }

  sealed trait Literal

  object Literal {

    case object Unit extends ResolvedAst.Literal

    case class Bool(literal: scala.Boolean) extends ResolvedAst.Literal

    case class Int(literal: scala.Int) extends ResolvedAst.Literal

    case class Str(literal: java.lang.String) extends ResolvedAst.Literal

    case class Tag(name: Name.Resolved, ident: ParsedAst.Ident, literal: ResolvedAst.Literal) extends ResolvedAst.Literal

    case class Tuple(elms: List[ResolvedAst.Literal]) extends ResolvedAst.Literal

  }

  sealed trait Expression extends Definition

  object Expression {

    case class Var(ident: ParsedAst.Ident) extends ResolvedAst.Expression

    case class Ref(name: Name.Resolved) extends ResolvedAst.Expression

    case class Lit(literal: ResolvedAst.Literal) extends ResolvedAst.Expression

    case class Lambda(formals: List[FormalArg], retTpe: ResolvedAst.Type, body: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class Apply(lambda: ResolvedAst.Expression, args: Seq[ResolvedAst.Expression]) extends ResolvedAst.Expression

    case class Unary(op: UnaryOperator, e: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class Binary(op: BinaryOperator, e1: ResolvedAst.Expression, e2: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class IfThenElse(e1: ResolvedAst.Expression, e2: ResolvedAst.Expression, e3: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class Let(ident: ParsedAst.Ident, value: ResolvedAst.Expression, body: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class Match(e: ResolvedAst.Expression, rules: Seq[(ResolvedAst.Pattern, ResolvedAst.Expression)]) extends ResolvedAst.Expression

    case class Tag(name: Name.Resolved, ident: ParsedAst.Ident, e: ResolvedAst.Expression) extends ResolvedAst.Expression

    case class Tuple(elms: List[ResolvedAst.Expression]) extends ResolvedAst.Expression

    case class Ascribe(e: ResolvedAst.Expression, tpe: ResolvedAst.Type) extends ResolvedAst.Expression

    case class Error(location: SourceLocation) extends ResolvedAst.Expression

  }

  /**
   * A common super-type for resolved patterns.
   */
  sealed trait Pattern extends ResolvedAst

  object Pattern {

    /**
     * An AST node representing a wildcard pattern.
     *
     * @param location the source location.
     */
    case class Wildcard(location: SourceLocation) extends ResolvedAst.Pattern

    /**
     * An AST node representing a variable pattern.
     *
     * @param ident the variable name.
     */
    case class Var(ident: ParsedAst.Ident) extends ResolvedAst.Pattern

    /**
     * An AST node representing a literal pattern.
     *
     * @param literal the literal.
     */
    case class Lit(literal: ResolvedAst.Literal) extends ResolvedAst.Pattern

    /**
     * An AST node representing a tagged pattern.
     *
     * @param name the name of the enum.
     * @param ident the name of the tag.
     * @param pat the nested pattern.
     */
    case class Tag(name: Name.Resolved, ident: ParsedAst.Ident, pat: ResolvedAst.Pattern) extends ResolvedAst.Pattern

    /**
     * An AST node representing a tuple pattern.
     *
     * @param elms the elements of the tuple.
     */
    case class Tuple(elms: List[ResolvedAst.Pattern]) extends ResolvedAst.Pattern

  }

  object Predicate {

    /**
     * A predicate that is allowed to occur in the head of a rule.
     *
     * @param name the name of the predicate.
     * @param terms the terms of the predicate.
     */
    case class Head(name: Name.Resolved, terms: List[ResolvedAst.Term.Head])

    /**
     * A predicate that is allowed to occur in the body of a rule.
     *
     * @param name the name of the predicate.
     * @param terms the terms of the predicate.
     */
    case class Body(name: Name.Resolved, terms: List[ResolvedAst.Term.Body])

  }

  object Term {

    /**
     * A common super-type for terms that are allowed appear in a head predicate.
     */
    sealed trait Head extends ResolvedAst

    object Head {

      /**
       * An AST node representing a variable term.
       *
       * @param ident the variable name.
       */
      case class Var(ident: ParsedAst.Ident) extends ResolvedAst.Term.Head

      /**
       * An AST node representing a literal term.
       *
       * @param literal the literal.
       */
      case class Lit(literal: ResolvedAst.Literal) extends ResolvedAst.Term.Head

      /**
       * An AST node representing a function call term.
       *
       * @param name the name of the called function.
       * @param args the arguments to the function.
       */
      case class Apply(name: Name.Resolved, args: List[ResolvedAst.Term.Head]) extends ResolvedAst.Term.Head

    }

    /**
     * A common super-type for terms that are allowed to appear in a body predicate.
     */
    sealed trait Body extends ResolvedAst

    object Body {

      /**
       * An AST node representing a wildcard term.
       *
       * @param location the location of the wildcard.
       */
      case class Wildcard(location: SourceLocation) extends ResolvedAst.Term.Body

      /**
       * An AST node representing a variable term.
       *
       * @param ident the variable name.
       */
      case class Var(ident: ParsedAst.Ident) extends ResolvedAst.Term.Body

      /**
       * An AST node representing a literal term.
       *
       * @param literal the literal.
       */
      case class Lit(literal: ResolvedAst.Literal) extends ResolvedAst.Term.Body

    }

  }

  /**
   * A common super-type for resolved types.
   */
  sealed trait Type extends ResolvedAst

  object Type {

    /**
     * An AST node representing the Unit type.
     */
    case object Unit extends ResolvedAst.Type

    /**
     * An AST node representing the Boolean type.
     */
    case object Bool extends ResolvedAst.Type

    /**
     * An AST node representing the Integer type.
     */
    case object Int extends ResolvedAst.Type

    /**
     * An AST node representing the String type.
     */
    case object Str extends ResolvedAst.Type

    /**
     * An AST node representing a type tag.
     *
     * @param name the name of the enum.
     * @param ident the name of the tag.
     * @param tpe the nested type.
     */
    case class Tag(name: Name.Resolved, ident: ParsedAst.Ident, tpe: ResolvedAst.Type) extends ResolvedAst.Type

    /**
     * An AST node representing a tuple type.
     *
     * @param elms the type of the elements.
     */
    case class Tuple(elms: List[ResolvedAst.Type]) extends ResolvedAst.Type

    /**
     * An AST node representing a function type.
     *
     * @param args the argument types.
     * @param retTpe the return type.
     */
    case class Function(args: List[ResolvedAst.Type], retTpe: ResolvedAst.Type) extends ResolvedAst.Type

  }

  /**
   * A resolved AST node representing a formal argument in a function.
   *
   * @param ident the name of the argument.
   * @param tpe the type of the argument.
   */
  case class FormalArg(ident: ParsedAst.Ident, tpe: ResolvedAst.Type) extends ResolvedAst

}