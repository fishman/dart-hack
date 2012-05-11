--------------rope.lua------------

-- Splay ropes
-- Implemented by Rici Lake; released into the public domain, April 2004.

-- The splay implementation was inspired by Chris Okasaki, and splay trees themselves
-- were invented by Daniel Sleator and Robert Tarjan.

-- I don't think the application to interval maps is original either,
-- but I thought it was interesting.

-- This is not an industrial strength implementation: splay is recursive and
-- the stack can overflow; moreover, a realistic implementation would probably
-- attempt to ensure a minimum node size, in part to avoid that problem.

-- There are lots of things which could be improved, if anyone feels like it:

--  1) I think concat is just awful. I'm not even sure if it even works. The original
--     version produced very unbalanced ropes, which led to a stack overflow, so I rewrote
--     it.

--  2) The more I think about it, the more I think the more normal interval representation
--     should be used: rope = tuple(size, left, string, right) where size is the total
--     size represented by the node. This would make rope.len() O(1), and somewhat simplify
--     concat (although it would require looking up child sizes a lot, which is why I didn't
--     do it.

--  3) Many routines should handle strings and ropes interchangeably. This would slow things
--     down a bit, but it would make the interface less surprising. (In particular, there is
--     currently no rope concatenator, although it is easy enough to do:
--       rope.replace(b, 1, 0, a) produces a ++ b

--  4) It would be useful to have optimised versions of splay_to_min and splay_to_max.

--  5) Representing the empty rope as nil was probably a mistake, although it simplified
--     some parts of the code. It almost certainly should be tuple(nil, "", 0, nil) or
--     something like that; and a single interned value should be used.

-- Here is the current interface:

-- rope = rope.new(string)   Create a new rope  

-- int = rope.len(rope)      Return length of rope (in characters)

-- rope = rope.from(rope, index)
--                           Return the suffix of the rope, starting at index
--                           index defaults to 1

-- rope = rope.upto(rope, index)
--                           Return the prefix of the rope, starting at index
--                           index defaults to len(rope)

-- rope = rope.sub(rope, low, high)
--                           Return the substring of the rope starting at low and ending
--                           at high; low defaults to 1 and high defaults to len(rope)

-- rope = rope.tconcat(table, sep, low, high)
--                           Same calling sequence as table.concat

-- rope = rope.concat(string, ...)
--                           Creates a rope by concatenating its arguments.

-- rope = rope.rep(string, count)
--                           Creates a rope of count repetitions of a string,
--                           maximising node sharing.

-- rope = rope.replace(rope, low, high, rope-or-string-or-nil)
--                           Creates a rope from the prefix of before low,
--                           the replacement, and the suffix after high. low defaults
--                           to 1 and high to len(rope)

-- for _, seg in rope.segs(rope) do ... end
--                           Sets seg to each string segment of the rope in order

-- string = rope.tostring(rope)
--                           Flattens a rope into a string

-- left, string, rpos, right = rope.splay(rope, index)
--                           (Use with caution, internal routine)
--                           left and right are the ropes to the left and right of
--                           the string; the string contains the character at index unless
--                           index is greater than the length of the rope. rpos is the
--                           character offset of right within the original rope.
--                           No argument checking is done; if rope is nil or index < 1,
--                           the function will bomb.

-- string = rope.view(rope)  (mostly for debugging). Shows the internal structure of
--                           a rope.

-- NOTES:
--  1) negative indexes do not work like they do in Lua. Perhaps they should. Currently,
--     negative indexes are treated as though they were < 1.
--  2) replace might not do what you think when high < low; the description above is accurate.
--       rope.replace(a, i, i - 1, b) inserts b just before index i
--       rope.replace(a, i, i - 2, b) duplicates index i - 1 so that it appears both before and
--                                    after the inserted rope/string.
--       rope.replace(a, rope.len(a) + 1, 0, b) returns a ++ b ++ a
--  3) ropes are functional objects; modifying a rope always creates a new rope. For example,
--     during an iteration using segs, modifications to the originally iterated rope will not
--     be visible.
 
local version = "0.2"

local slen, ssub = string.len, string.sub
local mfloor = math.floor

return function()
  local rope = {}

  -- splay(rope, pivot)
  -- returns r1, str, idx, r2 such that:
  --   either pivot is in str (starting at pivot - idx - 1) (yes, that is negative)
  --   or pivot > idx and r2 is nil
  -- This function will fail without warning if idx < 1; it is not intended for
  -- public consumption.
  local function splay(r, pivot)
    local a, s, x, b = r()
    if pivot <= x - slen(s) then
      local a1, t, y, a2 = a()
      if pivot <= y - slen(t) then
        local small, u, z, big = splay(a1, pivot)
        return small, u, z, tuple(big, t, y - z, tuple(a2, s, x - y, b))
       elseif pivot <= y then
        return a1, t, y, tuple(a2, s, x - y, b)
       else
        local small, u, z, big = splay(a2, pivot - y)
        return tuple(a1, t, y, small), u, y + z, tuple(big, s, x - y - z, b)
      end
     elseif pivot <= x or not b then return a, s, x, b
     else
      pivot = pivot - x
      local b1, t, y, b2 = b()
      if pivot <= y - slen(t) then
        local small, u, z, big = splay(b1, pivot)
        return tuple(a, s, x, small), u, x + z, tuple(big, t, y - z, b2)
       elseif pivot <= y or not b2 then
        return tuple(a, s, x, b1), t, x + y, b2
       else
        local small, u, z, big = splay(b2, pivot - y)
        return tuple(tuple(a, s, x, b1), t, x + y, small), u, x + y + z, big
      end
    end
  end
  
  local function len(r)
    local accum = 0
    while r do
      local _, _, x, b = r()
      r, accum = b, accum + x
    end
    return accum
  end
  
  local function new(s) 
    if s ~= "" then return tuple(nil, s, slen(s), nil) end
  end
  
  local function rr(a, b)
    if a and b then
      local b1, s, x, b2 = splay(b, 1)
      return tuple(a, s, x + len(a), b2)
     else
      return a or b
    end
  end

  local function rsr(a, s, b)
    if s == "" then return rr(a, b)
               else return tuple(a, s, len(a) + slen(s), b)
    end
  end
  
  local function upto(r, j)
    if r and j then
      if j <= 0 then return nil end
      local a, s, x, b = splay(r, j)
      if j <= x then r = tuple(a, ssub(s, 1, j - x - 1), j, nil) end
    end
    return r
  end

  local function from(r, i)
    if r and i and i > 0 then
      local a, s, x, b = splay(r, i)
      r = i <= x and tuple(nil, ssub(s, i - x - 1), x - i + 1, b) 
    end
    return r
  end

  local function sub(r, i, j)
    if i and j then
      if i <= j then return from(upto(r, j), i) end
     else return upto(r, i)
    end
  end

  local function replace(r, i, j, s)
    if not i or i < 1 then i = 1 end
    r = r and tuple(splay(r, i))
    if type(s) == "string" then
      return rsr(upto(r, i - 1), s, j and from(r, j + 1))
     else
      return rr(upto(r, i - 1), rr(s, j and from(r, j + 1)))
    end
  end

  -- This is just like table.concat; the table is a table of strings,
  -- and sep is also a string.

  -- The implementation of the auxiliary functions definitely shows why it
  -- might be more convenient to store total length instead of right-offset
  -- in individual nodes. However, that would complicate splay and sub.
  -- The awfulness of this function comes from the need to avoid introducing
  -- nodes with empty strings. Relaxing that constraint would make this
  -- function trivial, but there is no mechanism for getting rid of the
  -- empty strings and their presence would complicate everything else.
  
  -- Looking at this again, I think it's premature optimisation syndrome.
  -- It should just use rr and rsr -- that's what they are for, after all.
  -- Trying to squeeze a splay into an optimal binary tree just ain't worth it.
    
  local function concat(t, sep, st, fi)
    st, fi = st or 1, fi or table.getn(t)
    if st <= fi then
      if sep and sep ~= "" then
        local lsep = slen(sep)
        local function aux_concat(st, fi)
          if st == fi then return new(t[st]), slen(t[st])
           else
            local mid = mfloor((st + fi) * 0.5)
            local t1, len1 = aux_concat(st, mid)
            local t2, len2 = aux_concat(mid + 1, fi)
            return tuple(t1, sep, len1 + lsep, t2), len1 + lsep + len2
          end
        end
        return aux_concat(st, fi)
       else
        -- sep is the empty string. This is, bizarrely, more complicated
        -- The temptation to just call table.concat was almost overwhelming.

        -- precondition for the auxiliary: neither t[st] nor t[fi] are empty
        local function aux_concat_mt(st, fi)
          if st == fi then return new(t[st]), slen(t[st])
           else
            local mid1 = mfloor((st + fi) * 0.5)
            local mid2 = mid1 + 1
            while t[mid1] == "" do mid1 = mid1 - 1 end
            while t[mid2] == "" do mid2 = mid2 + 1 end
            if mid1 ~= st then mid1, mid = mid1 - 1, mid1
             elseif mid2 ~= fi then mid, mid2 = mid2, mid2 + 1
             else -- exactly two things
              return tuple(new(t[st]), t[fi], slen(t[st]) + slen(t[fi]), nil)
            end -- at least three things, one of which is at mid
              local t1, len1 = aux_concat_mt(st, mid1)
              local lmid = slen(t[mid])
              local t2, len2 = aux_concat_mt(mid2, fi)
              return tuple(t1, mid, lmid, t2), len1 + lmid + len2
            end
          end
        end

        -- Ditch empty strings at the margins
        while t[st] == "" do st = st + 1; if st > fi then return nil end
        while t[fi] == "" do fi = fi - 1 end -- t[st] is not empty
        return aux_concat_mt(st, fi)
      end
    end
  end

  -- This is just like string.rep; but it attempts to maximise node
  -- sharing, demonstrating a curious feature of functional splays
  local function rep(s, n)
    local rv
    if n > 0 then
      local lens = slen(s)
      local k, l, prev = 1, lens
      while k < n do
        prev = tuple(prev, s, l, prev)
        l, k = 2 * l, 2 * k + 1
      end
      rv = prev
      local lenr = (n + 1) * lens - l
      while lenr > 0 do
        repeat
          local a, s, x, b = prev()
          prev, l = a, x
        until l <= lenr
        lenr = lenr - l
        rv = tuple(prev, s, l, rv)
      end
    end
    return rv
  end

  local function nextseg(_, r)
    if r then
      local a, s, x, b = splay(r, 1)
      return b or false, s
    end
  end

  -- segs(r) returns an internal key and successive string segments of r;
  -- the key return should be ignored (it will be false on the last segment,
  -- but I am not going to guarantee that just yet.)
  
  -- If you can arrange for the argument to not be stored in a local variable,
  -- the iteration will not hold onto any reference to the rope, so gradual garbage
  -- collection would be possible.
   
  local function segs(r) return nextseg, nil, r end

  -- Unfortunately, tuples don't have metamethods. But this will flatten the
  -- rope. For outputting, for example, it is probably better to pass
  -- successive segments to an output function.
  local function tostring(r)
    local t = {n=0}
    for _, s in segs(r) do table.insert(t, s) end
    return table.concat(t)
  end

  -- debug
  local function view(r)
    if r then
      local a, s, x, b = r()
      return string.format("(%s %q:%d %s)", view(a), s, x, view(b))
     else
      return "-"
    end
  end

  local rope = {
    __VERSION = version,
    new = new,
    len = len,
    from = from,
    upto = upto,
    sub = sub,
    tconcat = concat,
    concat = function(...) return concat(arg) end,
    rep = rep,
    replace = replace,
    segs = segs,
    tostring = tostring,
    splay = splay,
    view = view
  }

  return rope
end
