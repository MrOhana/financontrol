# AI Coding Standards & Rules

1.  **Avoid Fully Qualified Class Names**: Always import classes and use their simple names, unless there is a naming conflict (e.g., `java.util.Date` vs `java.sql.Date`).
    *   *Bad*: `java.text.NumberFormat nf = ...`
    *   *Good*: `NumberFormat nf = ...` (with `import java.text.NumberFormat;`)
