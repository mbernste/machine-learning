#!/usr/bin/env ruby

#usage
if ARGV.length != 1
  puts "Usage: Requires the file to be converted as an argument"
  exit 1
end

#format filename
input = ARGV[0]
output = input
if input =~ /\./
  output = output.gsub(/\..*/,'.arff')
else
  input += ".csv"
  output += ".arff"
end

#get file
fileLines = open(input).read.split "\n"
fileLines.each_with_index do |line,index|
  fileLines[index] = line.split ","
  fileLines[index].each_with_index do |value,i|
    fileLines[index][i] = value.strip
  end
end

#handle missing data
#remove instance?
fileLines = fileLines.select { |line| !(line.include?("") || line.include?("?")) }

#get structure
attributes = []
fileLines.first.length.times do 
  attributes << []
end
fileLines.each_with_index do |instance,lineNum|
  if lineNum > 0 #skip header
    instance.each_with_index do |value,index|
      attributes[index] << value unless value.empty?
    end
  end
end

#find continuous features and bin
def is_numeric? thing
  true if Float(thing) rescue false
end

attributes.each_with_index do |att,index|
  if att.uniq.length >= 15
    found_string = false
    att.each do |val|
      found_string ||= !is_numeric?(val)
    end

    if !found_string
      att.map!{|val| val.to_f}
      att.sort!
      #create bins
      num_bins = 4
      per_bin = att.length / num_bins
      bins = []
      val_index = 0
      while val_index < att.length
        bins << "#{att[val_index]}_#{att[[val_index + per_bin - 1,att.length-1].sort.first]}"
        val_index += per_bin
        old = att[val_index - 1]
        while val_index < att.length and att[val_index] == old
          val_index += 1
        end
      end
      attributes[index] = bins

      #modify features
      fileLines.each_with_index do |line, line_num|
        if line_num > 0
          old_val = fileLines[line_num][index]
          bins.each do |range|
            range_vals = range.split "_"
            if old_val.to_f >= range_vals[0].to_f and old_val.to_f <= range_vals[1].to_f
               fileLines[line_num][index] = range
            end
          end
        end
      end
    end
  end
end


#remove duplicates and sort
attributes.each_with_index do |values, index|
  attributes[index] = values.uniq.sort
end
#write to arff
f = File.open(output, 'w') 

#attributes
fileLines.first.each_with_index do |name,index|
  size = attributes[index].length
  f.write "@ATTRIBUTE \"#{name}\" { #{attributes[index][0..size].join(",")} }\n"
end

#data
f.write "@DATA\n"
size = fileLines.length
fileLines[1..size].each do |data|
  f.write "#{data.join ","}\n"
end

f.close
