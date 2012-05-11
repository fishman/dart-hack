---------kmp.lua-----------
--
-- A small example of using ropes (and maybe not a very good one)

local slen, ssub = string.len, string.sub

return function()
  -- returns a function which matches p; it accepts characters one
  -- at a time, and returns the length of the match (i.e. string.len(p))
  -- when it is fed the last matching character.
  
  -- The algorithm was taken more or less straight out of Sedgewick,
  -- adjusted to be 1-based and to be inline rather than a string scan.
  -- There are ways to make this work with wildcards and even intervals,
  -- but this is just an example.
  local function kmp(p)
    -- We compute the transition vector and other goodies, and then
    -- return a custom matcher function. It is convenient to also
    -- split the pattern into a table
    local P, M = {}, slen(p); for i = 1, M do P[i] = ssub(p, i, i) end
    local i, j, next = 1, 0, {0}
    while i < M do
      while j > 0 and P[i] ~= P[j] do j = next[j] end
      i, j = i + 1, j + 1
      next[i] = (P[i] == P[j] and next[j]) or j
    end

    j = 1  -- used as closure
    return function(ch)
      while j > 0 and ch ~= P[j] do j = next[j] end
      j = j + 1
      if j > M then j = 1; return M end
    end
  end

  -- Given a rope r, a pattern string p and a replacement string q,
  -- returns a rope with all occurrences of p to q.
  -- (p and q are strings, not ropes, but that could be fixed easily enough.)
  local function replaceAll(r, p, q)
    local scanner = kmp(p)
    local j, offset = 0, slen(q) - slen(p)
    for _, seg in rope.segs(r) do
      for i = 1, slen(seg) do
        local m = scanner(ssub(seg, i, i))
        if m then
          r = rope.replace(r, j + i - m + 1, j + i, q)
          j = j + offset
        end
      end
      j = j + slen(seg)
    end
    return r
  end    

  local sr = {
    kmp = kmp,
    replaceAll = replaceAll
  }
  
  return sr
end
