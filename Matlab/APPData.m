clc; clear;close all;

a = textread('app4.txt');
a = a(10:end);

plot(-a)
