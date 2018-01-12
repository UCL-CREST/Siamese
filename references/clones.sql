-- MAIN QUERY
SELECT * FROM
  (
    SELECT
      function_id_one,
      sum(syntactic_type) AS typesum,
      count(*)            AS rowcount
    FROM (clones
      INNER JOIN functions ON clones.function_id_one = functions.id)
    WHERE
      functions.endline - functions.startline + 1 >= 10
      AND (similarity_token >= 0.7 OR similarity_line >= 0.7)
    GROUP BY function_id_one
  ) AS c
WHERE c.typesum < 3 * c.rowcount
  AND c.rowcount >= 10
  AND c.rowcount <= 20
-- LIMIT 100
;

-- FOLLOW-UP QUERY
SELECT A.id as id1, A.name as file1, A.type as type1, A.startline as start1, A.endline as end1,
  B.id as id2, B.name as file2, B.type as type2, B.startline as start2, B.endline as end2, clones.syntactic_type
FROM clones
  INNER JOIN functions AS A ON function_id_one = A.id
  INNER JOIN functions AS B ON function_id_two = B.id
WHERE A.endline - A.startline + 1 >= 10
  AND (similarity_token >= 0.7 OR similarity_line >= 0.7)
  AND function_id_one = 9217
ORDER BY clones.syntactic_type ASC, B.name ASC
;


-- OTHER QUERIES
SELECT *
FROM (clones INNER JOIN functions ON clones.function_id_one = functions.id)
WHERE
  functions.endline - functions.startline + 1 >= 10
--   AND syntactic_type <= 1
--   AND syntactic_type <= 2
--   AND syntactic_type = 3
  AND (similarity_token >= 0.7 OR similarity_line >= 0.7);


SELECT * FROM
(
  SELECT function_id_one, count(*) AS count
  FROM (clones INNER JOIN functions ON clones.function_id_one = functions.id)
  WHERE
    functions.endline - functions.startline + 1 >= 10
  --   AND syntactic_type <= 1
  --   AND syntactic_type <= 2
  --   AND syntactic_type = 3
    AND (similarity_token >= 0.7 OR similarity_line >= 0.7)
  GROUP BY function_id_one
) AS A
WHERE
  count >= 10
  AND count <= 50
LIMIT 50;
;

-- SELECT DISTINCT function_id_one
-- FROM clones INNER JOIN functions ON clones.function_id_one = functions.id
-- WHERE syntactic_type = 2
-- AND functions.endline - functions.startline + 1 >= 10;

-- SELECT count(*) FROM
--   (
--     SELECT *
--     FROM clones
--       INNER JOIN functions AS A ON function_id_one = A.id
--       INNER JOIN functions AS B ON function_id_two = B.id
--     WHERE syntactic_type = 2
--       AND A.endline - A.startline + 1 >= 10
--       AND B.endline - B.startline + 1 >= 10
--   ) AS A;

-- SELECT *
-- FROM functions
-- WHERE name='26758.java';


