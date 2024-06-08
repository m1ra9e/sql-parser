SELECT
  t.col1,
  t.col2 AS qwerty, 

  (SELECT o.c1 FROM hidden.secret_table o LIMIT 1),

  (SELECT
     w.c1,
     (SELECT y.c1 FROM very_hidden.very_secret_table y LIMIT 1) AS b
   FROM other_hidden.other_secret_table w LIMIT 1) AS m,

   (SELECT
      h.c1,
      (SELECT j.c1 FROM very_hidden.very_secret_table j LIMIT 1) AS r
    FROM 
      (SELECT q1, q2
       FROM
         hidden_fromer.hide_table
      ) h LIMIT 1)

FROM
  (SELECT p.col1, p.col2
   FROM
     (SELECT k.col1, k.col2, k.col3
      FROM
        public.some_table k
     ) p
  ) t;